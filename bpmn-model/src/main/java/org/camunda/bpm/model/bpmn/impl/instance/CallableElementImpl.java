/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.model.bpmn.impl.instance;

import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.child.ChildElement;
import org.camunda.bpm.model.xml.type.child.ChildElementCollection;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.ElementReferenceCollection;

import java.util.Collection;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;
import static org.camunda.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;

/**
 * The BPMN callableElement element
 *
 * @author Daniel Meyer
 * @author Sebastian Menski
 */
public class CallableElementImpl extends RootElementImpl implements CallableElement {

  private static Attribute<String> nameAttribute;
  private static ElementReferenceCollection<Interface, SupportedInterfaceRef> supportedInterfaceRefCollection;
  private static ChildElement<IoSpecification> ioSpecificationChild;
  private static ChildElementCollection<IoBinding> ioBindingCollection;

  public static void registerType(ModelBuilder bpmnModelBuilder) {
    ModelElementTypeBuilder typeBuilder = bpmnModelBuilder.defineType(CallableElement.class, BPMN_ELEMENT_CALLABLE_ELEMENT)
      .namespaceUri(BPMN20_NS)
      .extendsType(RootElement.class)
      .instanceProvider(new ModelTypeInstanceProvider<ModelElementInstance>() {
        public ModelElementInstance newInstance(ModelTypeInstanceContext instanceContext) {
          return new CallableElementImpl(instanceContext);
        }
      });

    nameAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_NAME)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    supportedInterfaceRefCollection = sequenceBuilder.elementCollection(SupportedInterfaceRef.class)
      .qNameElementReferenceCollection(Interface.class)
      .build();

    ioSpecificationChild = sequenceBuilder.element(IoSpecification.class)
      .build();

    ioBindingCollection = sequenceBuilder.elementCollection(IoBinding.class)
      .build();

    typeBuilder.build();
  }

  public CallableElementImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public String getName() {
    return nameAttribute.getValue(this);
  }

  public void setName(String name) {
    nameAttribute.setValue(this, name);
  }

  public Collection<Interface> getSupportedInterfaces() {
    return supportedInterfaceRefCollection.getReferenceTargetElements(this);
  }

  public IoSpecification getIoSpecification() {
    return ioSpecificationChild.getChild(this);
  }

  public void setIoSpecification(IoSpecification ioSpecification) {
    ioSpecificationChild.setChild(this, ioSpecification);
  }

  public Collection<IoBinding> getIoBindings() {
    return ioBindingCollection.get(this);
  }

}
