package com.qncube.lcommon

import java.lang.Exception

class RtcException(val code: Int, val msg: String) : Exception(msg)