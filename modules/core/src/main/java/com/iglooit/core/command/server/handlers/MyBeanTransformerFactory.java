package com.iglooit.core.command.server.handlers;


import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;

public class MyBeanTransformerFactory implements CustomBeanTransformerSpi.Factory
{
    public CustomBeanTransformerSpi newCustomBeanTransformer(BeanTransformerSpi beanTransformer)
    {
        return new MyBeanTransformer();
    }
}
