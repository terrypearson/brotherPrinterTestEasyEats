# brotherPrinterTestEasyEats
A quick test of brother QL-700W printer functionalities.

This will probably work with other printers as well. 

A couple of notes, changing the paper size breaks this app. I know, it is
dumb, but you really need to be specific with this. We are using the Dk-2205 2.4" continuous paper. You will need to change the
rather cryptic line `printInfo.labelNameIndex = LabelInfo.QL700.W62.ordinal();` for the app if you choose different paper. The
W62 refers to the width (62mm). You will have to investigate if you have something different.

As far as I can tell, brother's library files break when using Android 7. You get an ERROR_INTERNAL_ERROR that comes from the brother
Library file. But this should work with the Brother thermal printer with Android 5, and possibly Android 6 (untested as of yet).

You will need at least Android 5, because we are generating a pdf (Native PDF Renderer was introduced in 5). Once Brother updates
their libraries to support 7, I will try to include them here as well.
