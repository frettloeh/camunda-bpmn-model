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
package org.camunda.bpm.xml.model.impl.type.child;

import org.camunda.bpm.xml.model.Model;
import org.camunda.bpm.xml.model.ModelException;
import org.camunda.bpm.xml.model.impl.ModelBuildOperation;
import org.camunda.bpm.xml.model.impl.type.ModelElementTypeImpl;
import org.camunda.bpm.xml.model.impl.type.reference.ElementReferenceCollectionBuilderImpl;
import org.camunda.bpm.xml.model.impl.type.reference.QNameElementReferenceCollectionBuilderImpl;
import org.camunda.bpm.xml.model.instance.ModelElementInstance;
import org.camunda.bpm.xml.model.type.ChildElementCollectionBuilder;
import org.camunda.bpm.xml.model.type.ChildElementCollection;
import org.camunda.bpm.xml.model.type.ModelElementType;
import org.camunda.bpm.xml.model.type.reference.ElementReferenceCollectionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Meyer
 *
 */
public class ChildElementCollectionBuilderImpl<T extends ModelElementInstance> implements ChildElementCollectionBuilder<T>, ModelBuildOperation {

  /** The {@link ModelElementType} of the element containing the collection */
  final ModelElementTypeImpl containingType;
  private final ChildElementCollectionImpl<T> collection;
  final Class<T> childElementType;

  private ElementReferenceCollectionBuilder<?, ?> referenceBuilder;

  private final List<ModelBuildOperation> modelBuildOperations = new ArrayList<ModelBuildOperation>();

  public ChildElementCollectionBuilderImpl(Class<T> childElementType, String localName, String namespaceUri, ModelElementType containingType) {
    this.childElementType = childElementType;
    this.containingType = (ModelElementTypeImpl) containingType;
    this.collection = createCollectionInstance(localName, namespaceUri);
  }

  public ChildElementCollectionBuilderImpl(Class<T> type, ModelElementType containingType) {
    this.childElementType = type;
    this.containingType = (ModelElementTypeImpl) containingType;
    this.collection = createCollectionInstance(type);
  }

  ChildElementCollectionImpl<T> createCollectionInstance(Class<T> type) {
    return new TypedChildElementCollectionImpl<T>(type, containingType);
  }

  ChildElementCollectionImpl<T> createCollectionInstance(String localName, String namespaceUri) {
    return new NamedChildElementCollection<T>(localName, namespaceUri, containingType);
  }

  public ChildElementCollectionBuilder<T> immutable() {
    collection.setImmutable();
    return this;
  }

  public ChildElementCollectionBuilder<T> maxOccurs(int i) {
    collection.setMaxOccurs(i);
    return this;
  }

  public ChildElementCollectionBuilder<T> minOccurs(int i) {
    collection.setMinOccurs(i);
    return this;
  }

  public ChildElementCollection<T> build() {
    return collection;
  }

  @Override
  public <V extends ModelElementInstance> ElementReferenceCollectionBuilder<V,T> qNameElementReferenceCollection(Class<V> referenceTargetType) {
    QNameElementReferenceCollectionBuilderImpl<V,T> builder = new QNameElementReferenceCollectionBuilderImpl<V,T>(childElementType, referenceTargetType, collection, containingType);
    setReferenceBuilder(builder);
    return builder;
  }

  @Override
  public <V extends ModelElementInstance> ElementReferenceCollectionBuilder<V, T> idElementReferenceCollection(Class<V> referenceTargetType) {
    ElementReferenceCollectionBuilder<V,T> builder = new ElementReferenceCollectionBuilderImpl<V,T>(childElementType, referenceTargetType, collection);
    setReferenceBuilder(builder);
    return builder;
  }

  void setReferenceBuilder(ElementReferenceCollectionBuilder<?, ?> referenceBuilder) {
    if (this.referenceBuilder != null) {
      throw new ModelException("An collection cannot have more than one reference");
    }
    this.referenceBuilder = referenceBuilder;
    modelBuildOperations.add(referenceBuilder);
  }

  public void performModelBuild(Model model) {
    ModelElementType elementType = model.getType(childElementType);
    if(elementType == null) {
      throw new ModelException(containingType+" declares undefined child element of type "+childElementType+".");
    }
    containingType.registerChildElementType(elementType);
    for (ModelBuildOperation modelBuildOperation : modelBuildOperations) {
      modelBuildOperation.performModelBuild(model);
    }
  }

}
