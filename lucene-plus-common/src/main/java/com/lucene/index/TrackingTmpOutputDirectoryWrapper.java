/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lucene.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.lucene.store.Directory;
import com.lucene.store.FilterDirectory;
import com.lucene.store.IOContext;
import com.lucene.store.IndexInput;
import com.lucene.store.IndexOutput;

final class TrackingTmpOutputDirectoryWrapper extends FilterDirectory {
  private final Map<String,String> fileNames = new HashMap<>();

  TrackingTmpOutputDirectoryWrapper(Directory in) {
    super(in);
  }

  @Override
  public IndexOutput createOutput(String name, IOContext context) throws IOException {
    IndexOutput output = super.createTempOutput(name, "", context);
    fileNames.put(name, output.getName());
    return output;
  }

  @Override
  public IndexInput openInput(String name, IOContext context) throws IOException {
    String tmpName = fileNames.get(name);
    return super.openInput(tmpName, context);
  }

  public Map<String, String> getTemporaryFiles() {
    return fileNames;
  }
}
