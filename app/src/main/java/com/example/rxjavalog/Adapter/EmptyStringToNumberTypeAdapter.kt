package com.example.rxjavalog.Adapter

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException

class EmptyStringToNumberTypeAdapter : TypeAdapter<Number>() {
    override fun write(jsonWriter: JsonWriter?, number: Number?) {
        if (number == null){
            jsonWriter?.nullValue()
            return
        }
        jsonWriter?.value(number)
    }

    override fun read(jsonReader: JsonReader?): Number? {
        if (jsonReader?.peek() == JsonToken.NULL){
            jsonReader.nextNull()
            return null
        }
        try {
            val value : String? = jsonReader?.nextString()
            if ("" == value){
                return 0
            } else if ("0.00" == value){
                return 0
            }
            return Integer.parseInt(value!!)
        } catch (e : NumberFormatException){
            throw JsonSyntaxException(e)
        }
    }
}