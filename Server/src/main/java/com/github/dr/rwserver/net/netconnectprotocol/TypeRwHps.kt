/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.rwserver.net.netconnectprotocol

import com.github.dr.rwserver.data.global.Data
import com.github.dr.rwserver.io.Packet
import com.github.dr.rwserver.net.core.TypeConnect
import com.github.dr.rwserver.net.core.server.AbstractNetConnect
import com.github.dr.rwserver.util.PacketType
import com.github.dr.rwserver.util.Time.millis

class TypeRwHps : TypeConnect {
    @Throws(Exception::class)
    override fun typeConnect(con: AbstractNetConnect, packet: Packet) {
        con.setLastReceivedTime()
        if (!Data.game.oneReadUnitList) {
            if (packet.type == PacketType.PACKET_ADD_GAMECOMMAND) {
                con.receiveCommand(packet)
                con.player!!.lastMoveTime = millis()
            } else {
                when (packet.type) {
                    PacketType.PACKET_PREREGISTER_CONNECTION -> con.registerConnection(packet)
                    PacketType.PACKET_PLAYER_INFO -> if (!con.getPlayerInfo(packet)) {
                        con.disconnect()
                    }
                    PacketType.PACKET_HEART_BEAT_RESPONSE -> {
                        val player = con.player
                        player!!.ping = (System.currentTimeMillis() - player.timeTemp).toInt() shr 1
                    }
                    PacketType.PACKET_ADD_CHAT -> con.receiveChat(packet)
                    PacketType.PACKET_DISCONNECT -> con.disconnect()
                    PacketType.PACKET_ACCEPT_START_GAME -> con.player!!.start = true
                    PacketType.PACKET_SERVER_DEBUG -> con.debug(packet)
                    PacketType.PACKET_SYNC -> Data.game.gameSaveCache = packet

                    118 -> con.sendRelayServerTypeReply(packet)

                    else -> {
                    }
                }
            }
        } else {
            when (packet.type) {
                160 -> {
                    con.sendRelayServerInfo()
                    con.sendRelayServerCheck()
                }
                152 -> {
                    //con.sendRelayServerType();
                    //break;
                    //case 118:
                    con.sendRelayServerId()
                    //break;
                    //case 141:
                    con.sendRelayPlayerInfo()
                }
                175 -> con.getRelayUnitData(packet)
                else -> {
                }
            }
        }
    }

    override val version: String
        get() = "2.0.0"
}