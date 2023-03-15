package com.example.flows_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        //////////  cold stream ///////////////
        val job= GlobalScope.launch {
            val data:Flow<Int> =producer()
//            consumer
            data.collect{
                Log.d("SUBHAJIT 1 :",it.toString())
            }
        }
        job.start()
/*
//        cancel the corotines after 3500 ms from the start of the job
        GlobalScope.launch {
            delay(3500)
            job.cancel()
        }
 */
        GlobalScope.launch {
            val data:Flow<Int> =producer()
            delay(2500)
//            consumer
            data.collect{
                Log.d("SUBHAJIT 2 :",it.toString())
            }
        }



 */
//     Kotlin Flow Operators - Terminal + Map, Filter Buffer Operators
        GlobalScope.launch (Dispatchers.Main) {
            /*  ////////  Operators
            producer()
                .onStart {
//                    visible the loader in UI
                    emit(-1)
                    Log.d("SUBHAJIT ","starting out")
                }
                .onCompletion {
//                    invisible the loader and show the data
                    emit(6)
                    Log.d("SUBHAJIT ","completed")
                }
                .onEach {
                    Log.d("SUBHAJIT ","About to emit  $it")
                }
//                    terminal operator which are suspend function
                .collect{
                    Log.d("SUBHAJIT :",it.toString())
                }

             */


            /*
           /////// return the list of the item of the producer//

                val list=producer().toList()
                Log.d("SUBHAJIT :",list.toString())

             */
            ////////////  Map
            /*        producer()
                .map {
                it*2
                }
                .filter {
                   it<8
                }
                .collect{
                    Log.d("SUBHAJIT :",it.toString())
                }
     */
            /*
            getnote()
                .map {
                    FormattedNote(it.isActive,it.title,it.desc)
                }
                .filter {
                    it.isActive
                }
                .collect{
                    Log.d("SUBHAJIT :",it.toString())
                }
       */
            /*
            /////////////  Buffer
//            when producer is fast but consumer is slow
//            then we used buffer to store a specifying items for certain time
//            doesn't execute from the 1st
            val time= measureTimeMillis {
                producer()
                    .buffer(3)
                    .collect{
                        delay(1500)
                        Log.d("SUBHAJIT :",it.toString())
                    }
            }
            Log.d("SUBHAJIT :",time.toString())
        }

          */
            /*
            try {
                producer()
//                        context switching
                    .flowOn(Dispatchers.IO)
                    .collect {
                        delay(1500)
                        Log.d("SUBHAJIT :", " collector thread ${Thread.currentThread().name}")
                    }
            } catch (e: Exception) {
                    Log.d("TAG",e.message.toString())
            }

             */
//            val res=producer_Mutable_dharedflow()
            val  res=stateflow()
            delay(6000)
            res.collect{
                Log.d("TAG","item $it")
            }
        }
//        GlobalScope.launch(Dispatchers.Main) {
//            val res=producer_Mutable_dharedflow()
//            delay(2500)
//            res.collect{
//                Log.d("TAG","item $it")
//            }
//        }
    }
//it's a hot stream which start which data are executed when multiple consumer are executes
    private fun producer_Mutable_dharedflow(): Flow<Int> {
//    replay shows previous numbers of items
            val Mutable_dharedflow= MutableSharedFlow<Int>(2)
            GlobalScope.launch {
                val list= listOf(1,2,3,4,5,6,7,8,9,0)
                list.forEach{
                    Mutable_dharedflow.emit(it)
                    delay(1000)
                }
            }
        return Mutable_dharedflow
    }
//    State Flow maintains the last value or the latest value emitted in the flow.
//    State Flow is also a shared flow but with single buffer to keep the conflated values in the flow.
    private fun stateflow():Flow<Int>{
            val mutableStateFlow= MutableStateFlow(10)
        GlobalScope.launch {
            delay(2000)
            mutableStateFlow.emit(20)
            delay(2000)
            mutableStateFlow.emit(30)
        }
        return mutableStateFlow
    }
    //    producer
   /* private fun producer()= flow<Int> {
        val list= listOf(1,2,3,4,5,6,7,8,9,0)
        list.forEach{
            delay(1000)
            emit(it)
        }

    */
        fun producer():Flow<Int>{
            return flow <Int>{
                val list= listOf(1,2,3,4,5,6,7,8,9,0)
                list.forEach{
                    delay(1000)
                    Log.d("TAG","Emitter Thread ${Thread.currentThread().name}")
                    emit(it)
                    throw Exception("Error in emitter")
                }
//  handle the exception of flow for this function
            }.catch {
                Log.d("TAG", "Emitter catch ${it.toString()}")
                emit(-1)
            }
        }
    }


    private fun getnote():Flow<Note>
    {
        val list= listOf(
            Note(1,true,"subhajit","my name"),
            Note(2,false,"sudip","my frnd"),
            Note(3,true,"prasenjt","my brother")
        )
        return list.asFlow()
    }
data class Note(val id:Int,val isActive:Boolean,val title:String,val desc:String)
data class FormattedNote(val isActive:Boolean,val title:String,val desc:String)