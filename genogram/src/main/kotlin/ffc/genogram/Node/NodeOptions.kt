/*
 * Copyright 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.genogram.Node

import ffc.genogram.GenderLabel
import ffc.genogram.Node.Node.Companion.nodeBorderSize
import ffc.genogram.Person

fun setNodeSize(nodeName: String): String {

    if (nodeName.length > Node.nodeSize) {
        return nodeName.subSequence(0, Node.nodeSize.toInt()) as String
    } else {
        var tmp = ""
        val diff = Math.abs(nodeName.length - Node.nodeSize.toInt())
        for (i in 0 until diff / 2) {
            tmp += " "
        }

        var node = tmp + nodeName + tmp
        val addMoreSpace = Node.nodeSize.toInt() - node.length
        if (addMoreSpace > 0)
            for (i in 0 until addMoreSpace)
                node += " "

        return node
    }
}

// find the person index
fun findPersonPosition(personLayer: ArrayList<String>, focusedPerson: Person): Int {

    personLayer.forEachIndexed { index, element ->
        if (element == setNodeSize(focusedPerson.firstname))
            return index
    }

    return 0
}

fun createEmptyNode(): String {

    var result = ""

    for (i in 0 until Node.nodeSize.toInt() + nodeBorderSize.toInt()) {
        result += " "
    }

    return result
}

fun createGenderBorder(name: String, gender: GenderLabel): String {
    return if (gender == GenderLabel.MALE)
        "[${setNodeSize(name)}]"
    else
        "(${setNodeSize(name)})"
}
