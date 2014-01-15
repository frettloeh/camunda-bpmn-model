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

package org.camunda.bpm.model.core.impl.type.reference;

import org.camunda.bpm.model.core.ModelReferenceException;
import org.camunda.bpm.model.core.impl.instance.ModelElementInstanceImpl;
import org.camunda.bpm.model.core.impl.type.attribute.AttributeImpl;
import org.camunda.bpm.model.core.instance.ModelElementInstance;
import org.camunda.bpm.model.core.type.Attribute;
import org.camunda.bpm.model.core.type.ModelElementType;
import org.camunda.bpm.model.core.type.reference.AttributeReference;

/**
 * @author Sebastian Menski
 */
public class AttributeReferenceImpl<T extends ModelElementInstance> extends ReferenceImpl<T> implements AttributeReference<T> {

  protected final AttributeImpl<String> referenceSourceAttribute;

  public AttributeReferenceImpl(AttributeImpl<String> referenceSourceAttribute) {
    this.referenceSourceAttribute = referenceSourceAttribute;
  }

  public String getReferenceIdentifier(ModelElementInstance referenceSourceElement) {
    return referenceSourceAttribute.getValue(referenceSourceElement);
  }

  public void setReferenceIdentifier(ModelElementInstance referenceSourceElement, String referenceIdentifier) {
    referenceSourceAttribute.setValue(referenceSourceElement, referenceIdentifier);
  }

  /**
   * Get the reference source attribute
   *
   * @return the reference source attribute
   */
  public Attribute<String> getReferenceSourceAttribute() {
    return referenceSourceAttribute;
  }

  protected ModelElementType getReferenceSourceElementType() {
    return referenceSourceAttribute.getOwningElementType();
  }

  protected void updateReference(ModelElementInstance referenceSourceElement, String oldIdentifier, String newIdentifier) {
    String referencingAttributeValue = getReferenceIdentifier(referenceSourceElement);
    if(oldIdentifier != null && oldIdentifier.equals(referencingAttributeValue)) {
      setReferenceIdentifier(referenceSourceElement, newIdentifier);
    }
  }

  protected void removeReference(ModelElementInstance referenceSourceElement) {
    referenceSourceAttribute.removeAttribute(referenceSourceElement);
  }

  @SuppressWarnings("unchecked")
  protected T resolveReference(ModelElementInstanceImpl referenceSourceElement, String referenceIdentifier) {
    String identifier = getReferenceIdentifier(referenceSourceElement);

    ModelElementInstance referenceTargetElement = referenceSourceElement.getModelInstance().getModelElementById(identifier);
    if(referenceTargetElement != null) {
      try {
        return (T) referenceTargetElement;

      } catch(ClassCastException e) {
        throw new ModelReferenceException("Element " + referenceSourceElement + " references element " + referenceTargetElement + " of wrong type. "
            + "Expecting " + referenceTargetAttribute.getOwningElementType() + " got " + referenceTargetElement.getElementType());
      }
    } else {
      return null;
    }
  }
}