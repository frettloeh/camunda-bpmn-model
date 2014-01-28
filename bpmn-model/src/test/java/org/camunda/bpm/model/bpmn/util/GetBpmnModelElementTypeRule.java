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

package org.camunda.bpm.model.bpmn.util;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Sebastian Menski
 */
public class GetBpmnModelElementTypeRule extends TestWatcher {

  private ModelInstance modelInstance;
  private Model model;
  private ModelElementType modelElementType;

  @Override
  @SuppressWarnings("unchecked")
  protected void starting(Description description) {
    String className = description.getClassName();
    className =  className.replaceAll("Test", "");
    Class<? extends ModelElementInstance> instanceClass = null;
    try {
      instanceClass = (Class<? extends ModelElementInstance>) Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    modelInstance = Bpmn.createEmptyModel();
    model = modelInstance.getModel();
    modelElementType = model.getType(instanceClass);
  }

  public ModelInstance getModelInstance() {
    return modelInstance;
  }

  public Model getModel() {
    return model;
  }

  public ModelElementType getModelElementType() {
    return modelElementType;
  }
}
