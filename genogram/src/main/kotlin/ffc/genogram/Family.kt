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

package ffc.genogram

class Family(
    var familyId: Long,
    var familyName: String,
    var bloodFamily: List<Int>?,
    var member: List<Person>?
) {

    fun removeBloodFamily(personId: Long) {
        val tmp: MutableList<Int> = bloodFamily as MutableList<Int>
        if (tmp.size > 1) {
            tmp.find { it == personId.toInt() }?.let {
                tmp.remove(it)
            }
            bloodFamily = tmp
        } else if (tmp.size == 1) {
            bloodFamily = null
        }
    }

}
