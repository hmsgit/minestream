/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrapper.generator;

import weka.core.Instance;

/**
 *
 * @author mahmud
 */
public class InstancePool {
    public Instance _inst;
    public int _pool;
    
    public InstancePool(Instance inst, int pool) {
        _inst = inst; _pool = pool;
    }
}
