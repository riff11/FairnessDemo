/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.concurrency.fairness;

/**
 *
 * @author igor
 */
public interface IProducer {
    public void produce(Consumer consumer);
    public void setContinueFlag(boolean value);
}
