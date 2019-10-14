package org.sedly.sigh.shader;

import java.util.Map;

public interface Uniform {

  String getName();

  Map<String, Object> getValue();

}
