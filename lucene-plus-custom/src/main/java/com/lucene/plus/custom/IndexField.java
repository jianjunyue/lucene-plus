package com.lucene.plus.custom;

public class IndexField {

    /*
     * 索引主键
     */
    private String Name;
    /*
     * yes:索引 no：不索引
     */
    private IndexType Index;

    /*
     * yes:分词 no:不分词
     */
    private IndexType Analyzed;

    /*
     * yes:存储 no:不存储
     */
    private IndexType Store;

    /*
     * 域加权
     */
    private Integer Weight;

    private FieldType fieldType;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public IndexType getIndex() {
        return Index;
    }

    public void setIndex(IndexType index) {
        Index = index;
    }

    public IndexType getAnalyzed() {
        return Analyzed;
    }

    public void setAnalyzed(IndexType analyzed) {
        Analyzed = analyzed;
    }

    public Integer getWeight() {
        return Weight;
    }

    public void setWeight(Integer weight) {
        Weight = weight;
    }

    public IndexType getStore() {
        return Store;
    }

    public void setStore(IndexType store) {
        Store = store;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

}