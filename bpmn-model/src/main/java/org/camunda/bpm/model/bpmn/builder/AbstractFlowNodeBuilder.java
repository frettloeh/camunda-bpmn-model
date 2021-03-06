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

package org.camunda.bpm.model.bpmn.builder;

import org.camunda.bpm.model.bpmn.BpmnModelException;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

/**
 * @author Sebastian Menski
 */
public abstract class AbstractFlowNodeBuilder<B extends AbstractFlowNodeBuilder<B, E>, E extends FlowNode> extends AbstractFlowElementBuilder<B, E> {

  private SequenceFlowBuilder currentSequenceFlowBuilder;

  protected AbstractFlowNodeBuilder(BpmnModelInstance modelInstance, E element, Class<?> selfType) {
    super(modelInstance, element, selfType);
  }

  private SequenceFlowBuilder getCurrentSequenceFlowBuilder() {
    if (currentSequenceFlowBuilder == null) {
      SequenceFlow sequenceFlow = createSibling(SequenceFlow.class, null);
      currentSequenceFlowBuilder = sequenceFlow.builder();
    }
    return currentSequenceFlowBuilder;
  }

  public B condition(String name, String condition) {
    if (name != null) {
      getCurrentSequenceFlowBuilder().name(name);
    }
    ConditionExpression conditionExpression = modelInstance.newInstance(ConditionExpression.class);
    conditionExpression.setTextContent(condition);
    getCurrentSequenceFlowBuilder().condition(conditionExpression);
    return myself;
  }

  private void connectTarget(FlowNode target) {
    getCurrentSequenceFlowBuilder()
      .from(element)
      .to(target);

    currentSequenceFlowBuilder = null;
  }

  public B sequenceFlowId(String sequenceFlowId) {
    getCurrentSequenceFlowBuilder().id(sequenceFlowId);
    return myself;
  }

  private <T extends FlowNode> T createTarget(Class<T> typeClass, String identifier) {
    T target = createSibling(typeClass, identifier);
    connectTarget(target);
    return target;
  }

  public ServiceTaskBuilder serviceTask() {
    return createTarget(ServiceTask.class, null).builder();
  }

  public ServiceTaskBuilder serviceTask(String id) {
    return createTarget(ServiceTask.class, id).builder();
  }

  public SendTaskBuilder sendTask() {
    return createTarget(SendTask.class, null).builder();
  }

  public SendTaskBuilder sendTask(String id) {
    return createTarget(SendTask.class, id).builder();
  }

  public UserTaskBuilder userTask() {
    return createTarget(UserTask.class, null).builder();
  }

  public UserTaskBuilder userTask(String id) {
    return createTarget(UserTask.class, id).builder();
  }

  public BusinessRuleTaskBuilder businessRuleTask() {
    return createTarget(BusinessRuleTask.class, null).builder();
  }

  public BusinessRuleTaskBuilder businessRuleTask(String id) {
    return createTarget(BusinessRuleTask.class, id).builder();
  }

  public ScriptTaskBuilder scriptTask() {
    return createTarget(ScriptTask.class, null).builder();
  }

  public ScriptTaskBuilder scriptTask(String id) {
    return createTarget(ScriptTask.class, id).builder();
  }

  public ReceiveTaskBuilder receiveTask() {
    return createTarget(ReceiveTask.class, null).builder();
  }

  public ReceiveTaskBuilder receiveTask(String id) {
    return createTarget(ReceiveTask.class, id).builder();
  }

  public ManualTaskBuilder manualTask() {
    return createTarget(ManualTask.class, null).builder();
  }

  public ManualTaskBuilder manualTask(String id) {
    return createTarget(ManualTask.class, id).builder();
  }

  public EndEventBuilder endEvent() {
    return createTarget(EndEvent.class, null).builder();
  }

  public EndEventBuilder endEvent(String id) {
    return createTarget(EndEvent.class, id).builder();
  }

  public ParallelGatewayBuilder parallelGateway() {
    return createTarget(ParallelGateway.class, null).builder();
  }

  public ParallelGatewayBuilder parallelGateway(String id) {
    return createTarget(ParallelGateway.class, id).builder();
  }

  public ExclusiveGatewayBuilder exclusiveGateway() {
    return createTarget(ExclusiveGateway.class, null).builder();
  }

  public ExclusiveGatewayBuilder exclusiveGateway(String id) {
    return createTarget(ExclusiveGateway.class, id).builder();
  }

  public IntermediateCatchEventBuilder intermediateCatchEvent() {
    return createTarget(IntermediateCatchEvent.class, null).builder();
  }

  public IntermediateCatchEventBuilder intermediateCatchEvent(String id) {
    return createTarget(IntermediateCatchEvent.class, id).builder();
  }

  public CallActivityBuilder callActivity() {
    return createTarget(CallActivity.class, null).builder();
  }

  public CallActivityBuilder callActivity(String id) {
    return createTarget(CallActivity.class, id).builder();
  }

  public SubProcessBuilder subProcess() {
    return createTarget(SubProcess.class, null).builder();
  }

  public SubProcessBuilder subProcess(String id) {
    return createTarget(SubProcess.class, id).builder();
  }

  public Gateway findLastGateway() {
    FlowNode lastGateway = element;
    while (true) {
      try {
        lastGateway = lastGateway.getPreviousNodes().singleResult();
        if (lastGateway instanceof Gateway) {
          return (Gateway) lastGateway;
        }
      }
      catch(BpmnModelException e) {
        throw new BpmnModelException("Unable to determine an unique previous gateway of " + lastGateway.getId(), e);
      }
    }
  }

  public AbstractGatewayBuilder moveToLastGateway() {
    return findLastGateway().builder();
  }

  public AbstractFlowNodeBuilder moveToNode(String identifier) {
    ModelElementInstance instance = modelInstance.getModelElementById(identifier);
    if (instance != null && instance instanceof FlowNode) {
      return ((FlowNode) instance).builder();
    }
    else {
      throw new BpmnModelException("Flow node not found for id " + identifier);
    }
  }

  public AbstractFlowNodeBuilder connectTo(String identifier) {
    ModelElementInstance target = modelInstance.getModelElementById(identifier);
    if (target == null) {
      throw new BpmnModelException("Unable to connect " + element.getId() + " to element " + identifier + " cause it not exists.");
    }
    else if (!(target instanceof FlowNode)) {
      throw new BpmnModelException("Unable to connect " + element.getId() + " to element " + identifier + " cause its not a flow node.");
    }
    else {
      FlowNode targetNode = (FlowNode) target;
      connectTarget(targetNode);
      return targetNode.builder();
    }
  }

}
