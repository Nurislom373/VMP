import {StateAction} from "../services/state.action";

export class StateActionNode {

  private _key?: string;
  private _action?: StateAction;

  constructor(key: string, action: StateAction) {
    this._key = key;
    this._action = action;
  }

  get key(): string {
    return <string>this._key;
  }

  set key(value: string) {
    this._key = value;
  }

  get action(): StateAction {
    return <StateAction>this._action;
  }

  set action(value: StateAction) {
    this._action = value;
  }
}
