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
import org.camunda.bpm.model.xml.type.ModelElementTypeBuilder;
import org.camunda.bpm.model.xml.type.attribute.Attribute;
import org.camunda.bpm.model.xml.type.child.ChildElementCollection;
import org.camunda.bpm.model.xml.type.child.SequenceBuilder;

import java.util.Collection;

import static org.camunda.bpm.model.bpmn.impl.BpmnModelConstants.*;

/**
 * The BPMN definitions element
 *
 * @author Daniel Meyer
 * @author Sebastian Menski
 */
public class DefinitionsImpl extends BpmnModelElementInstanceImpl implements Definitions {

  private static Attribute<String> idAttribute;
  private static Attribute<String> nameAttribute;
  private static Attribute<String> targetNamespaceAttribute;
  private static Attribute<String> expressionLanguageAttribute;
  private static Attribute<String> typeLanguageAttribute;
  private static Attribute<String> exporterAttribute;
  private static Attribute<String> exporterVersionAttribute;

  private static ChildElementCollection<Import> importCollection;
  private static ChildElementCollection<Extension> extensionCollection;
  private static ChildElementCollection<RootElement> rootElementCollection;
  private static ChildElementCollection<Relationship> relationshipCollection;

  public static void registerType(ModelBuilder bpmnModelBuilder) {

    ModelElementTypeBuilder typeBuilder = bpmnModelBuilder.defineType(Definitions.class, BPMN_ELEMENT_DEFINITIONS)
      .namespaceUri(BPMN20_NS)
      .instanceProvider(new ModelElementTypeBuilder.ModelTypeInstanceProvider<Definitions>() {
        public Definitions newInstance(ModelTypeInstanceContext instanceContext) {
          return new DefinitionsImpl(instanceContext);
        }
      });

    idAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_ID)
      .idAttribute()
      .build();

    nameAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_NAME)
      .build();

    targetNamespaceAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_TARGET_NAMESPACE)
      .required()
      .build();

    expressionLanguageAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_EXPRESSION_LANGUAGE)
      .defaultValue(XPATH_NS)
      .build();

    typeLanguageAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_TYPE_LANGUAGE)
      .defaultValue(XML_SCHEMA_NS)
      .build();

    exporterAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_EXPORTER)
      .build();

    exporterVersionAttribute = typeBuilder.stringAttribute(BPMN_ATTRIBUTE_EXPORTER_VERSION)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    importCollection = sequenceBuilder.elementCollection(Import.class)
      .build();

    extensionCollection = sequenceBuilder.elementCollection(Extension.class)
      .build();

    rootElementCollection = sequenceBuilder.elementCollection(RootElement.class)
      .build();

    // TODO: add bpmndi:BPMNDiagram

    relationshipCollection = sequenceBuilder.elementCollection(Relationship.class)
      .build();

    typeBuilder.build();
  }

  public DefinitionsImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public String getId() {
    return idAttribute.getValue(this);
  }

  public void setId(String id) {
    idAttribute.setValue(this, id);
  }

  public String getName() {
    return nameAttribute.getValue(this);
  }

  public void setName(String name) {
    nameAttribute.setValue(this, name);
  }

  public String getTargetNamespace() {
    return targetNamespaceAttribute.getValue(this);
  }

  public void setTargetNamespace(String namespace) {
    targetNamespaceAttribute.setValue(this, namespace);
  }

  public String getExpressionLanguage() {
    return expressionLanguageAttribute.getValue(this);
  }

  public void setExpressionLanguage(String expressionLanguage) {
    expressionLanguageAttribute.setValue(this, expressionLanguage);
  }

  public String getTypeLanguage() {
    return typeLanguageAttribute.getValue(this);
  }

  public void setTypeLanguage(String typeLanguage) {
    typeLanguageAttribute.setValue(this, typeLanguage);
  }

  public String getExporter() {
    return exporterAttribute.getValue(this);
  }

  public void setExporter(String exporter) {
    exporterAttribute.setValue(this, exporter);
  }

  public String getExporterVersion() {
    return exporterVersionAttribute.getValue(this);
  }

  public void setExporterVersion(String exporterVersion) {
    exporterVersionAttribute.setValue(this, exporterVersion);
  }

  public Collection<Import> getImports() {
    return importCollection.get(this);
  }

  public Collection<Extension> getExtensions() {
    return extensionCollection.get(this);
  }

  public Collection<RootElement> getRootElements() {
    return rootElementCollection.get(this);
  }

  public Collection<Relationship> getRelationships() {
    return relationshipCollection.get(this);
  }

}
