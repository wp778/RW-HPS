/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.rwserver.util.alone

import com.github.dr.rwserver.core.thread.Threads.newThreadService2
import com.github.dr.rwserver.struct.Seq
import com.github.dr.rwserver.util.Time.getTimeFutureMillis
import com.github.dr.rwserver.util.Time.millis
import java.util.concurrent.TimeUnit

class BlackList {
    private val blackList = Seq<BlackData>(false, 16)
    fun addBlackList(str: String) {
        blackList.add(BlackData(str, getTimeFutureMillis(3600 * 1000L)))
    }

    fun containsBlackList(str: String): Boolean {
        val result = BooleanArray(1)
        blackList.each { e: BlackData ->
            if (e.ip == str) {
                result[0] = true
            }
        }
        return result[0]
    }

    private class BlackData(val ip: String, val time: Long) {
        override fun toString(): String {
            return ip
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            return if (other == null || javaClass != other.javaClass) {
                false
            } else ip == other.toString()
        }

        override fun hashCode(): Int {
            return ip.hashCode()
        }
    }

    init {
        newThreadService2({
            val time = millis()
            blackList.each({ e: BlackData -> e.time < time }) { value: BlackData -> blackList.remove(value) }
        }, 0, 1, TimeUnit.HOURS, "BlackListCheck")
    }
}