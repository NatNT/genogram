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

import ffc.genogram.FamilyTreeDrawer
import ffc.genogram.GenderLabel
import ffc.genogram.Person
import ffc.genogram.RelationshipLine.RelationshipLabel
import kotlin.math.PI

class FemaleNode(
    var familyTreeDrawer: FamilyTreeDrawer,
    private val addedPerson: Person,
    var focusedPerson: Person?,
    var nodeName: String,
    var parent: Person?
) : Node() {

    override fun drawNode(relationLabel: RelationshipLabel?, siblings: Boolean): FamilyTreeDrawer {

        if (relationLabel != RelationshipLabel.CHILDREN &&
            relationLabel != RelationshipLabel.TWIN
        ) {
            val addingLayer = familyTreeDrawer.findPersonLayer(focusedPerson!!)
            nodeName = createGenderBorder(nodeName, GenderLabel.FEMALE)

            if (focusedPerson != null) {

                // Find layer of AddedPerson's siblings
                val childrenLayer = familyTreeDrawer.findPersonLayer(focusedPerson!!)
                val childrenLineLayer = childrenLayer - 1

                // Find whether the focusedPerson has any siblings.
                // If he/she has we'll reorder the array.
                val hasRightHandSib = familyTreeDrawer.hasPeopleOnTheRight(
                    focusedPerson!!, addingLayer
                )

                if (hasRightHandSib) {
                    // FocusedPerson(addedPerson's husband) has older siblings
                    val addingInd = familyTreeDrawer.findPersonInd(
                        focusedPerson!!, addingLayer
                    )
                    familyTreeDrawer.addFamilyStorageReplaceIndex(
                        addingLayer, addingInd, nodeName, addedPerson
                    )

                    if (parent != null) {
                        // AddedPerson has no parents, focus only on draw "MarriageLine"
                        // Find AddedPerson's parent index
                        val parentLayer = familyTreeDrawer.findPersonLayer(parent!!)
                        val parentInd = familyTreeDrawer.findPersonIndById(focusedPerson!!.father!!, parentLayer)
                        // Find index of AddedPerson's siblings
                        val childrenNumber = familyTreeDrawer.findPersonLayerSize(childrenLayer)
                        val childrenListId = parent!!.children!!
                        val childrenListInd: MutableList<Int> = mutableListOf()
                        childrenListId.forEach { id ->
                            childrenListInd.add(
                                familyTreeDrawer.findPersonIndById(
                                    id.toLong(), childrenLayer
                                )
                            )
                        }

                        // Extend the MarriageLine of AddedPerson's parent.
                        if (childrenNumber > 3) {
                            // Extend the MarriageLine by adding the empty node(s).
                            var emptyNodeNumber = familyTreeDrawer.findNumberOfEmptyNode(parentLayer)
                            val addingEmptyNodes = findAddingEmptyNodes(childrenNumber)
                            familyTreeDrawer = addMoreNodes(
                                emptyNodeNumber, addingEmptyNodes, parentLayer, familyTreeDrawer
                            )
                        }

                        // Extend the ChildrenLine the top layer of the AddedPerson
                        val expectedLength = familyTreeDrawer.childrenLineLength(childrenNumber)
                        val extendedLine = familyTreeDrawer.extendLine(
                            expectedLength,
                            childrenListInd,
                            parentInd
                        )
                        familyTreeDrawer.replaceFamilyStorageIndex(
                            childrenLineLayer, parentLayer, extendedLine
                        )
                    }
                } else {
                    // When AddedPerson's husband is the youngest children.
                    // Add AddedPerson at the end of the layer.
                    familyTreeDrawer.addFamilyAtLayer(addingLayer, nodeName, addedPerson)

                    // Extend "MarriageLine" of the FocusedPerson's parents.
                    // Extend by adding the empty node(s).
                    if (parent != null) {
                        val parentLayer = familyTreeDrawer.findPersonLayer(parent!!)
                        val childrenNumber = familyTreeDrawer.findPersonLayerSize(childrenLayer)
                        var emptyNodeNumber = familyTreeDrawer.findNumberOfEmptyNode(parentLayer)
                        val addingEmptyNodes = findAddingEmptyNodes(childrenNumber)
                        familyTreeDrawer = addMoreNodes(
                            emptyNodeNumber, addingEmptyNodes, parentLayer, familyTreeDrawer
                        )

                        // Move children sign
                        val editedLine = familyTreeDrawer.moveChildrenLineSign(
                            childrenLineLayer, addingEmptyNodes
                        )
                        familyTreeDrawer.replaceFamilyStorageIndex(
                            childrenLineLayer, parentLayer, editedLine
                        )
                    }
                }
            } else {
                familyTreeDrawer.addFamilyLayer(nodeName, addedPerson)
            }
        } else {
            // Children or Twin
            val familyGen = familyTreeDrawer.findStorageSize() - 1
            familyTreeDrawer.addFamilyAtLayer(
                familyGen,
                setNodePosition(nodeName, GenderLabel.FEMALE, siblings),
                addedPerson
            )
        }

        return familyTreeDrawer
    }

    override fun getArea(): Double = PI * Math.pow(nodeSize, 2.0)
}
