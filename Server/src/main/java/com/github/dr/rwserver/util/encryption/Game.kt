/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.rwserver.util.encryption

/**
 * @author Dr.
 * @Data 2020/6/25 9:28
 */
object Game {
    @JvmStatic
	fun connectKey(paramInt: Int): String {
        return "c:" + paramInt + "m:" + (paramInt * 87 + 24) + "0:" + 44000 * paramInt + "1:" + paramInt + "2:" + 13000 * paramInt + "3:" + (28000 + paramInt) + "4:" + 75000 * paramInt + "5:" + (160000 + paramInt) + "6:" + 850000 * paramInt + "t1:" + 44000 * paramInt + "d:" + 5 * paramInt
    }
}