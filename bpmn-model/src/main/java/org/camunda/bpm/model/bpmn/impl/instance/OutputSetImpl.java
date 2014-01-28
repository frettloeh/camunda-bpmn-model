/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.DataInput;
import org.camunda.bpm.model.bpmn.instance.InputSet;
import org.camunda.bpm.model.bpmn.instance.OutputSet;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.ElementReferenceCollection;

import java.util.Collection;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;
import static org.camunda.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;

/**
 * The BPMN outputSet element
 *
 * @author Sebastian Menski
 */
public class OutputSetImpl extends BaseElementImpl implements OutputSet {

  private static Attribute<String> nameAttribute;
  private static ElementReferenceCollection<DataInput, DataOutputRefs> dataOutputRefsCollection;
  private static ElementReferenceCollection<DataInput, OptionalOutputRefs> optionalOutputRefsCollection;
  private static ElementReferenceCollection<DataInput, WhileExecutingOutputRefs> whileExecutingOutputRefsCollection;
  private static ElementReferenceCollection<InputSet, InputSetRefs>  inputSetInputSetRefsCollection;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(OutputSet.class, BPMN_ELEMENT_OUTPUT_SET)
      .namespaceUri(BPMN20_NS)
      .extendsType(BaseElement.class)
      .instanceProvider(new ModelTypeInstanceProvider<OutputSet>() {
        public OutputSet newInstance(ModelTypeInstanceContext instanceContext) {
          return new OutputSetImpl(instanceContext);
        }
      });

    nameAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_NAME)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    dataOutputRefsCollection = sequenceBuilder.elementCollection(DataOutputRefs.class)
      .idElementReferenceCollection(DataInput.class)
      .build();

    optionalOutputRefsCollection = sequenceBuilder.elementCollection(OptionalOutputRefs.class)
      .idElementReferenceCollection(DataInput.class)
      .build();

    whileExecutingOutputRefsCollection = sequenceBuilder.elementCollection(WhileExecutingOutputRefs.class)
      .idElementReferenceCollection(DataInput.class)
      .build();

    inputSetInputSetRefsCollection = sequenceBuilder.elementCollection(InputSetRefs.class)
      .idElementReferenceCollection(InputSet.class)
      .build();

    typeBuilder.build();
  }

  public OutputSetImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public String getName() {
    return nameAttribute.getValue(this);
  }

  public void setName(String name) {
    nameAttribute.setValue(this, name);
  }

  public Collection<DataInput> getDataOutputRefs() {
    return dataOutputRefsCollection.getReferenceTargetElements(this);
  }

  public Collection<DataInput> getOptionalOutputRefs() {
    return optionalOutputRefsCollection.getReferenceTargetElements(this);
  }

  public Collection<DataInput> getWhileExecutingOutputRefs() {
    return whileExecutingOutputRefsCollection.getReferenceTargetElements(this);
  }

  public Collection<InputSet> getInputSetRefs() {
    return inputSetInputSetRefsCollection.getReferenceTargetElements(this);
  }
}
