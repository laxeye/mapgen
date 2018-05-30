package ru.lgm.mapgen.main

import java.io.File

val barcodeMap = createBarcodeMap("barcodes.txt")

class MFString(
	var lgmID: String = "",
	val bcID: String = "",
	val comment: String = ""
){
	var atlasID: String = ""
	var bcSeq: String = ""
	var repl: String = "N/A"

	init {
		val bcArr = bcID.split(" ").map{it.toUpperCase()}
		val (bcFID, bcRID) = if (bcArr[0][0] == 'F') Pair(bcArr[0].drop(1), bcArr[1].drop(1)) else
			Pair(bcArr[1].drop(1), bcArr[0].drop(1))
		
		
		if ("repl" in lgmID){
			val zlp: List<String> = lgmID.split(".")
			repl = zlp.last()
			lgmID = zlp.dropLast(1).joinToString(".", "")
		}

		if (barcodeMap.containsKey(bcRID) and barcodeMap.containsKey(bcFID)){
			bcSeq = barcodeMap.get(bcRID) + barcodeMap.get(bcFID)
		}else{
			System.err.println("Error! Primer $bcRID or $bcFID not found for sample $lgmID!")
		}
		
		
	}
}


fun incorrectLine(i: Int, line: String, cause: String){
		System.err.println("Line $i: \"$line\" is incorrect! $cause!")
	}

fun createBarcodeMap(fn: String): Map<String,String>{
	val barcodeMap = File(fn).readLines().map{it.split("\t")[0] to it.split("\t")[1]}.toMap()
	return barcodeMap
}

fun createShitMap(fn: String): Map<String, String> {
	val readyMap = File(fn).readLines().map{it.split("\t")[1] to it.split("\t")[0]}.toMap()
	//val shitMap = pooIDList.associate(Pair(it.lgmID, it.atlasID)) 
	//Reserve
	return readyMap
}

fun rpbc(fn: String): MutableList<MFString> {
	
	System.err.println("Checking mapping file: " + fn)
	
	val lines = File(fn).readLines()
	val pooList = mutableListOf<MFString>()

	var lineN: Int = 0
	val primerSet: MutableSet<String> = mutableSetOf()
	val idSet: MutableSet<String> = mutableSetOf()

	val primerFMap: MutableMap<String,MutableSet<String>> = mutableMapOf()
	val primerRMap: MutableMap<String,MutableSet<String>> = mutableMapOf()
	
	lines.forEach {
			lineN++
			if("\t" in it){
				val (id, primers) = it.split("\t")
				if(" " in primers){
					val (fidx, ridx) = primers.split(" ")
					val smpl = id.split(".")[0]
					if(smpl in primerFMap){
						if (fidx in primerFMap.getValue(smpl)){
							incorrectLine(lineN, it, "Same forward \"$fidx\" for $smpl")
						}else{
							primerFMap.getValue(smpl).add(fidx)
						}
					}else{
						primerFMap.put(smpl,mutableSetOf())
					}

					if(smpl in primerRMap){
						if (ridx in primerRMap.getValue(smpl)){
							incorrectLine(lineN, it, "Same reverse \"$ridx\" for $smpl")
						}else{
							primerRMap.getValue(smpl).add(ridx)
						}
					}else{
						primerRMap.put(smpl,mutableSetOf())
					}
					if (primerSet.contains(primers)){
						incorrectLine(lineN, it, "Primers \"$primers\" are already in use")
					}
					if (idSet.contains(id)){
						incorrectLine(lineN, it, "ID \"$id\" was seen before!")
					}
					idSet.add(id)
					primerSet.add(primers)
					pooList.add(MFString(id.replace(" ", "."), primers))
				}else{
					incorrectLine(lineN, it, "Missing space between primers")
				}
			}else{
				incorrectLine(lineN, it, "Missing tab")
			}
	
	}

	System.err.println(fn + " proceed");
	return pooList
}


fun genmf(fn1: String, fn2: String) {
	val shitBCList = rpbc(fn1)
	val shitMap = createShitMap(fn2)
	val firstLine = "#SampleID\tBarcodeSequence\tLinkerPrimerSequence\tDescription"
	println(firstLine)
	fun getAtlas(lgm: String) = if (shitMap.containsKey(lgm)) shitMap.get(lgm) else lgm
	//leave lgm id if not found in Map
	shitBCList.forEach{
		if (it.repl == "N/A"){
			println(getAtlas(it.lgmID) + "\t" + it.bcSeq + "\t\t" + it.lgmID)
		}else{
			println(getAtlas(it.lgmID) + "." + it.repl + "\t" + it.bcSeq + "\t\t" + it.lgmID + "." + it.repl)
		}
		
	}
}


fun main(args: Array<String>) {
	val helpMessage: String = "Mapping file generator for QIIME.\n\n" +
	"Available options are:\n" +
	"h(elp) - print short help message\n" +
	"genmf <File lgm ID/Primers> <File atlas ID/lgm ID> - generate a mapping file\n" +
	"check <File ID/Primers - check primers for duplication.\n" +
	"Plese provide valid files for mapping file generatiion.\n" +
	"Check README.TXT for further information and examples.";

	when(args.size){
		2 -> {
				val (task, fn1) = args
				if (task == "check"){
					System.err.println("Checking primers.")
					rpbc(fn1)
				}else{
					System.err.println("Unknown task!\nNothing to do!\n$helpMessage")
				}
			}
		3 -> {
			val (task, fn1, fn2) = args;
			if (task == "genmf"){
				System.err.println("Generating mapping file.")
				genmf(fn1, fn2)
			}else{
				System.err.println("Unknown task!\nNothing to do!\n$helpMessage")
			}
		}
		else -> System.err.println("Incorrect parameters!\n$helpMessage")
	}
}
