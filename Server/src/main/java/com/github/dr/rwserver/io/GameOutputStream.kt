/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.rwserver.io

import com.github.dr.rwserver.io.output.DisableSyncByteArrayOutputStream
import com.github.dr.rwserver.util.zip.gzip.GzipEncoder
import java.io.DataOutputStream
import java.io.IOException

/**
 * @author Dr
 */
class GameOutputStream {
    private val buffer = DisableSyncByteArrayOutputStream()
    private val stream = DataOutputStream(buffer)

    fun createPacket(): Packet {
        try {
            stream.use { buffer.use {
                stream.flush()
                buffer.flush()
                return Packet(0, buffer.toByteArray())
            }}
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun createPacket(type: Int): Packet {
        try {
            stream.use { buffer.use {
                stream.flush()
                buffer.flush()
                return Packet(type, buffer.toByteArray())
            }}
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun createPackets(type: Int): Packet {
        try {
            stream.use { buffer.use {
                stream.flush()
                buffer.flush()
                return Packet(type, buffer.toByteArray())
            }}
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun getPacketBytes(): ByteArray {
        try {
            stream.use { buffer.use {
                stream.flush()
                buffer.flush()
                return buffer.toByteArray()
            }}
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    fun writeByte(value: Int) {
        stream.writeByte(value)
    }

    @Throws(IOException::class)
    fun writeBytes(value: ByteArray) {
        buffer.writeBytes(value)
    }

    @Throws(IOException::class)
    fun writeBoolean(value: Boolean) {
        stream.writeBoolean(value)
    }

    @Throws(IOException::class)
    fun writeInt(value: Int) {
        stream.writeInt(value)
    }

    @Throws(IOException::class)
    fun writeShort(value: Short) {
        stream.writeShort(value.toInt())
    }

    @Throws(IOException::class)
    fun writeFloat(value: Float) {
        stream.writeFloat(value)
    }

    @Throws(IOException::class)
    fun writeLong(value: Long) {
        stream.writeLong(value)
    }

    @Throws(IOException::class)
    fun writeString(value: String) {
        stream.writeUTF(value)
    }

    @Throws(IOException::class)
    fun writeIsString(gameInputStream: GameInputStream) {
        if (gameInputStream.readBoolean()) {
            writeBoolean(true)
            writeString(gameInputStream.readString())
        } else {
            writeBoolean(false)
        }
    }

    @Throws(IOException::class)
    fun transferTo(inputStream: GameInputStream) {
        inputStream.transferTo(buffer)
    }

    @Throws(IOException::class)
    fun transferToFixedLength(inputStream: GameInputStream, length: Int) {
        inputStream.transferToFixedLength(buffer,length)
    }

    @Throws(IOException::class)
    fun flushEncodeData(enc: GzipEncoder) {
        enc.close()
        writeString(enc.str!!)
        writeInt(enc.buffer.size())
        enc.buffer.writeTo(stream)
        stream.flush()
    }

    @Throws(IOException::class)
    fun flushMapData(mapSize: Int, bytes: ByteArray) {
        writeInt(mapSize)
        stream.write(bytes)
    }
}