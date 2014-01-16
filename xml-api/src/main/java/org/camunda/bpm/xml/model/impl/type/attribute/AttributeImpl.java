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
package org.camunda.bpm.xml.model.impl.type.attribute;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.xml.model.impl.type.reference.ReferenceImpl;
import org.camunda.bpm.xml.model.instance.ModelElementInstance;
import org.camunda.bpm.xml.model.type.Attribute;
import org.camunda.bpm.xml.model.type.ModelElementType;
import org.camunda.bpm.xml.model.type.reference.Reference;

/**
 * <p>Base class for implementing primitive value attributes</p>
 *
 * @author Daniel Meyer
 *
 */
public abstract class AttributeImpl<T> implements Attribute<T> {

  /** the local name of the attribute */
  private String attributeName;

  /** the namespace for this attribute */
  private String namespaceUri;

  /** the default value for this attribute: the default value is returned
   * by the {@link #getValue(ModelElementInstance)} method in case the attribute is not set on the
   * domElement.
   */
  private T defaultValue;

  private boolean isRequired = false;

  private boolean isIdAttribute = false;

  private final List<Reference<?>> outgoingReferences = new ArrayList<Reference<?>>();

  private final List<Reference<?>> incomingReferences = new ArrayList<Reference<?>>();

  private final ModelElementType owningElementType;

  AttributeImpl(ModelElementType owningElementType) {
    this.owningElementType = owningElementType;
  }

  /**
   * to be implemented by subclasses: converts the raw (String) value of the
   * attribute to the type required by the model
   *
   * @return the converted value
   */
  protected abstract T convertXmlValueToModelValue(String rawValue);

  /**
   * to be implemented by subclasses: converts the raw (String) value of the
   * attribute to the type required by the model
   *
   * @return the converted value
   */
  protected abstract String convertModelValueToXmlValue(T modelValue);

  public ModelElementType getOwningElementType() {
    return owningElementType;
  }

  /**
   * returns the value of the attribute.
   *
   * @return the value of the attribute.
   */
  public T getValue(ModelElementInstance modelElement) {
    String value;
    if(namespaceUri == null) {
      value = modelElement.getAttributeValue(attributeName);
    } else {
      value = modelElement.getAttributeValueNs(attributeName, namespaceUri);
    }

    // default value
    if(value == null && defaultValue != null) {
      return defaultValue;
    } else {
      return convertXmlValueToModelValue(value);
    }
  }

  /**
   * sets the value of the attribute.
   *
   *  the value of the attribute.
   */
  public void setValue(ModelElementInstance modelElement, T value) {
    String xmlValue = convertModelValueToXmlValue(value);
    if(namespaceUri == null) {
      modelElement.setAttributeValue(attributeName, xmlValue, isIdAttribute);
    } else {
      modelElement.setAttributeValueNs(attributeName, namespaceUri, xmlValue, isIdAttribute);
    }
  }

  public void updateIncomingReferences(ModelElementInstance modelElement, String newIdentifier, String oldIdentifier) {
    if (!incomingReferences.isEmpty()) {
      for (Reference<?> incomingReference : incomingReferences) {
        ((ReferenceImpl<?>) incomingReference).referencedElementUpdated(modelElement, oldIdentifier, newIdentifier);
      }
    }
  }

  public void setDefaultValue(T defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   */
  public void setRequired() {
    this.isRequired = true;
  }

  /**
   * @param namespaceUri the namespaceUri to set
   */
  public void setNamespaceUri(String namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  /**
   * @return the namespaceUri
   */
  public String getNamespaceUri() {
    return namespaceUri;
  }

  /**
   * @return the attributeName
   */
  public String getAttributeName() {
    return attributeName;
  }

  public boolean isIdAttribute() {
    return isIdAttribute;
  }

  /**
   * @param attributeName the attributeName to set
   */
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public void removeAttribute(ModelElementInstance modelElement) {
    if (namespaceUri == null) {
      modelElement.removeAttribute(attributeName);
    }
    else {
      modelElement.removeAttributeNs(attributeName, namespaceUri);
    }
  }

  public void unlinkReference(ModelElementInstance modelElement) {
    if (!incomingReferences.isEmpty()) {
      for (Reference<?> incomingReference : incomingReferences) {
        ((ReferenceImpl<?>) incomingReference).referencedElementRemoved(modelElement);
      }
    }
  }

  /**
   * Indicate whether this attribute is an Id attribute
   *
   */
  public void setId() {
    this.isIdAttribute = true;
  }

  /**
   * @return the incomingReferences
   */
  public List<Reference<?>> getIncomingReferences() {
    return incomingReferences;
  }

  /**
   * @return the outgoingReferences
   */
  public List<Reference<?>> getOutgoingReferences() {
    return outgoingReferences;
  }

  public void registerOutgoingReference(Reference<?> ref) {
    outgoingReferences.add(ref);
  }

  public void registerIncoming(Reference<?> ref) {
    incomingReferences.add(ref);
  }

}
