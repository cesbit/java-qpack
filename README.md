# QPack Java
QPack is a fast and efficient serialization format like MessagePack. One key difference is flexible map and array support which allows to write directly to a qpack buffer without the need to know the size for the map or array beforehand.


## Installation
You can download the latest version [here](https://github.com/transceptor-technology/java-qpack/releases/latest). But you can also clone this repository and open the project in your favorite Java IDE. After building the project, grab the ```java-qpack.jar``` file and add it to your own project as library.

## Pack
Create an instance of QPack and use its pack method

```qpack.pack(Object data);```

## Unpack
When unpack is called with only the first parameter, each String will be returned as bytes.

```qpack.unpack(byte[] qp, String decoder);```

## Example
```java
package qpack-test

import java.util.Arrays;
import transceptor.technology.QPack;

public class QPackTest() {

    public static void main() {
        QPack qpack = new QPack();

        String[] data = new String[]{"test", "qpack", "Java"};

        byte[] qp = qpack.pack(data);

        Object[] result = (Object[]) qpack.unpack(qp, "utf-8");

        System.out.println(Arrays.toString(result));
    }

}
```
