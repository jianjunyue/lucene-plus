package org.java.plus.dag.core.engine.dag;

import org.java.plus.dag.core.base.model.DataSet;
import org.java.plus.dag.core.base.model.ProcessorConfig;
import org.java.plus.dag.core.base.model.ProcessorContext;
import org.java.plus.dag.core.base.model.Row;
import org.java.plus.dag.core.base.proc.BaseProcessor;

import java.util.Arrays;
import java.util.Map;

/**
 * ����:
 * <p>
 * org.java.plus.dag.engine.dag.FakeProcessorAForTest
 *
 * @author jaye
 * @date 2018/10/23
 * <p>
 * config_start: |org.java.plus.dag.engine.dag.FakeProcessorAForTest||jaye|
 * config_end:
 */
public class FakeProcessorAForTest extends BaseProcessor {

    public FakeProcessorAForTest() {
        System.out.println("constructorA called");
    }

    @Override
    public void init(ProcessorConfig processorConfig) {
        System.out.println("initA");
    }

    @Override
    public Map<String, DataSet<Row>> process(ProcessorContext processorContext, Map<String, DataSet<Row>> dataSetMap) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("processA");
        dataSetMap.put("A", new DataSet<>());

        DataSet<Row> result = new DataSet<>();
        Row row = new Row();
        row.setId("a");
        result.setData(Arrays.asList(row));
        dataSetMap.put(getMainChainKey(), result);

        return dataSetMap;
    }

/*
    @Override
    public String getInstanceKey() {
        return "a";
    }*/

    @Override
    public int getProcessorTimeout() {
        return 2010;
    }

    @Override
    public void setProcessorTimeout(int processorTimeout) {

    }
}