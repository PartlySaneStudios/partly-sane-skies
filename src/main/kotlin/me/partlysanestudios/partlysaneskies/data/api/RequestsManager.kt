//
// Written by Su386.
// See LICENSE for copyright and license notices.
//


package me.partlysanestudios.partlysaneskies.data.api

import me.partlysanestudios.partlysaneskies.PartlySaneSkies
import me.partlysanestudios.partlysaneskies.utils.MathUtils
import java.io.IOException
import java.util.*

object RequestsManager {

    private val requestsQueue: Queue<Request> = LinkedList()
    private var lastRequestTime: Long = 0


    val thread = Thread({
        while(true) {
            try {
                run()

            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {

            }

            try {
                Thread.sleep(50)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }, "Partly Sane Skies Request Manager").start()

    private fun run() {
        if (requestsQueue.isEmpty()) {
            return
        }

        // The time in milliseconds between requests
        val timeBetweenRequests = Math.round(PartlySaneSkies.config.timeBetweenRequests * 1000).toLong()
        // If the time has not elapsed between requests
        if (MathUtils.onCooldown(lastRequestTime, timeBetweenRequests)) {
            return
        }

        // Requests and removes the 1st element in the queue
        val element = requestsQueue.poll()
        // If the request is supposed to run in the main thread
        lastRequestTime = PartlySaneSkies.getTime()
        if (element == null) {
            return
        }
        if (element.isMainThread()) {
            if (PartlySaneSkies.minecraft != null) {
                PartlySaneSkies.minecraft.addScheduledTask {
                    try {
                        element.startRequest()
                    } catch (e: IOException) {
                        element.setFailed("{THREW_IOEXEPCTION}")
                        e.printStackTrace()
                    }
                }

            } else {
                try {
                    element.startRequest()
                } catch (e: IOException) {
                    element.setFailed("{THREW_IOEXEPCTION}")
                    e.printStackTrace()
                }
            }

        } else {
            // Creates a new thread to execute request
            Thread(Runnable {
                try {
                    element.startRequest()
                } catch (e: IOException) {
                    element.setFailed("")
                    // If supposed to run in the next frame, run in the next frame
                    if (element.isRunNextFrame()) {
                        PartlySaneSkies.minecraft.addScheduledTask {
                            element.getWhatToRunWhenFinished()!!.run(element)
                        }
                        return@Runnable
                    }

                    // Runs on current thread
                    element.getWhatToRunWhenFinished()?.run(element)
                    e.printStackTrace()
                }
            }, "Request to ${element.getURL()}").start()
        }
    }

    /**
     * Adds a new request and returns its position in queue
     *
     * @param request the request to add
     */
    fun newRequest(request: Request) {
        requestsQueue.add(request)
    }
}