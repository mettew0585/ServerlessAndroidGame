package com.example.myapplication

class MapHandling {
    val SPLITLEN=36
    val EMAILLEN=18
    fun getAttractMapString(){

    }
    fun getUserColorToIdx(mapString:String,idx:Int) : Int{
        val tmpIdx=idx-1
        val tmpUserToken=mapString.chunked(SPLITLEN)
        val tmpUserIm=tmpUserToken[tmpIdx].split("/")
        val rtn= Integer.parseInt(tmpUserIm[1])
        return rtn
    }
    fun getMapColorToIdx(mapString:String,idx:Int):Int{
        val tmpIdx=idx-1
        val tmpUserToken=mapString.chunked(SPLITLEN)
        val tmpUserIm=tmpUserToken[tmpIdx].split("/")
        return Integer.parseInt(tmpUserIm[0])

    }
    fun getUserEmailToIdx(mapString:String,idx:Int):String{
        val tmpIdx=idx-1
        val tmpUserToken=mapString.chunked(SPLITLEN)
        val tmpUserIm=tmpUserToken[tmpIdx].split("/","#")
        return tmpUserIm[2]
    }
    fun UpdateMap(mapString:String,winnerNum:Int,loserNum:Int):String{
        var rtn:String
        val tmpUserToken=mapString.chunked(SPLITLEN)
        val tmpWinIdx=winnerNum-1
        val tmpLoserIdx=loserNum-1
        val tmpWinIm=tmpUserToken[tmpWinIdx].split("/")
        val tmpLoseIm=tmpUserToken[tmpLoserIdx].split("/")
        val tmpE:String =getUserEmailToIdx(mapString,winnerNum)+ "#############################"
        val tmpUserIm=(tmpWinIm[0]+"/"+tmpLoseIm[1]+"/"+tmpE).chunked(SPLITLEN)
        var tmpidx=0
        rtn=""
        while(tmpidx<25) {
            if (tmpidx != tmpLoserIdx)
                rtn = rtn + tmpUserToken[tmpidx]
            else
                rtn = rtn + tmpUserIm[0]
            tmpidx++
        }
        return rtn

    }
    fun getLandCount(mapString: String,idx:Int) : Int{
        var rtn=0
        for(i in 1..25){
            if(getUserColorToIdx(mapString,idx)==getMapColorToIdx(mapString,i))
                rtn++
        }

        return rtn
    }
    fun getAttackString(mapString:String):String{
        val token=mapString.chunked(SPLITLEN*25)
        return token[1]
    }

    fun changeAttackString(mapString:String,aIdx:Int,bIdx:Int,type:Int):String{
        val tmptoken=mapString.chunked(SPLITLEN*25)
        val token=tmptoken[1].chunked(1)
        var rtn=tmptoken[0]

        for(i in 0..24){
            val idx=i+1
            if(aIdx==getMapColorToIdx(mapString,idx)){
                rtn=rtn+type.toString()
            }else if(bIdx==getMapColorToIdx(mapString,idx)){
                rtn=rtn+type.toString()
            }else{
                rtn=rtn+token[i]
            }
        }
        return rtn
    }
}