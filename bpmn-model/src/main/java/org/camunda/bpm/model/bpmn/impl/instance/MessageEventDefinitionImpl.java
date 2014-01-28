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

import org.camunda.bpm.model.bpmn.instance.EventDefinition;
import org.camunda.bpm.model.bpmn.instance.Message;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.Operation;
import org.camunda.bpm.model.xml.ModelBuilder;
import org.camunda.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;
import org.camunda.bpm.model.xml.type.reference.AttributeReference;
import org.camunda.bpm.model.xml.type.reference.ElementReference;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;

/**
*
* @author Sebastian Menski
*
*/
public class MessageEventDefinitionImpl extends EventDefinitionImpl implements MessageEventDefinition {

  private static AttributeReference<Message> messageRefAttribute;
  private static ElementReference<Operation, OperationRef> operationRefChild;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(MessageEventDefinition.class, BPMN_ELEMENT_MESSAGE_EVENT_DEFINITION)
      .namespaceUri(BPMN20_NS)
      .extendsType(EventDefinition.class)
      .instanceProvider(new ModelElementTypeBuilder.ModelTypeInstanceProvider<MessageEventDefinition>() {
        public MessageEventDefinition newInstance(ModelTypeInstanceContext instanceContext) {
          return new MessageEventDefinitionImpl(instanceContext);
        }
      });

    messageRefAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_MESSAGE_REF)
      .qNameAttributeReference(Message.class)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    operationRefChild = sequenceBuilder.element(OperationRef.class)
      .qNameElementReference(Operation.class)
      .build();

    typeBuilder.build();
  }

  public MessageEventDefinitionImpl(ModelTypeInstanceContext context) {
    super(context);
  }

  public Message getMessage() {
    return messageRefAttribute.getReferenceTargetElement(this);
  }

  public void setMessage(Message message) {
    messageRefAttribute.setReferenceTargetElement(this, message);
  }

  public Operation getOperation() {
    return operationRefChild.getReferenceTargetElement(this);
  }

  public void setOperation(Operation operation) {
    operationRefChild.setReferenceTargetElement(this, operation);
  }

}
