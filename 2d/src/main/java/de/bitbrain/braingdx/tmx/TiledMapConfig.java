/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitbrain.braingdx.tmx;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains naming configuration for TMX maps.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class TiledMapConfig {

   private final Map<String, String> translations = new HashMap<String, String>();

   public TiledMapConfig translate(String key, String newKey) {
      translations.put(key, newKey);
      return this;
   }

   public String get(String key) {
      String newKey = translations.get(key);
      return newKey != null ? newKey : key;
   }
}
