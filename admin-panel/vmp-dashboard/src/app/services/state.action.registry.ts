import {Injectable} from "@angular/core";
import {StateActionNode} from "../models/state.action.node";
import {StateAction} from "./state.action";

@Injectable({
  providedIn: 'root'
})
export class StateActionRegistry {

  actions: StateActionNode[] = []

  /**
   *
   * @param node
   */
  public add(node: StateActionNode) {
    node !== null && this.actions.push(node);
  }

  /**
   *
   * @param key
   */
  public remove(key: string) {
    this.actions.forEach((item, index) => {
      if (item.key === key) this.actions.splice(index, 1);
    });
  }

  /**
   *
   * @param key
   */
  public executeAction(key: string) {
    let stateAction = this.getAction(key);
    stateAction.action();
  }

  /**
   *
   * @param key
   */
  public getAction(key: string): StateAction {
    return this.get(key).action;
  }

  /**
   *
   * @param key
   */
  public get(key: string): StateActionNode {
    return this.actions.filter(node => {
      return node.key === key;
    })[0];
  }
}
