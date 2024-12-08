import eu.mihosoft.vrl.v3d.*

//println "Clearing the Vitamins cache to make sure current geometry is being used (only run this operation when the STL has changed)"
//Vitamins.clear()

println "Loading piece STL from repo"
File pieceSTL = ScriptingEngine.fileFromGit(
	"https://github.com/JansenSmith/TechOrnament.git",
	"TechOrnament.stl");

println "Loading the CSG from the disk and caching it in memory"
CSG piece  = Vitamins.get(pieceSTL);
piece = piece.toZMin()

println "Loading signature CSG via factory"
CSG sig =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/JMS.git", // git location of the library
	                              "JMS.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )

println "Moving signature into position"
sig = sig.toZMin().rotz(-15).movex(piece.totalX/2)
sig = sig.mirrorx()
def sig_scale_factor = 0.75
sig = sig.scalex(sig_scale_factor).scaley(sig_scale_factor)
sig = sig.movex(-15).movey(-41)

//println "Differencing sig from the piece"
//piece = piece.difference(sig)

println "Adding loopy-loop"
def loop = new Cylinder(2,0.48,(int)64).toCSG()
				.difference(new Cylinder(1,0.48,(int)64).toCSG())
				.toZMin()
				.movey(46)
piece = piece.union(loop)

//println "Creating a tiny slice to get rid of the lip"
//def diff = new Cube(piece.totalX+1, piece.totalY+1, 0.1).toCSG()
//println "Removing the difference"
//piece = piece.difference(diff)

println "Setting CSG attributes"
piece = piece.setColor(javafx.scene.paint.Color.DARKGRAY)
			.setName("TechOrnament")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

println "Done!"
return [piece, sig]

