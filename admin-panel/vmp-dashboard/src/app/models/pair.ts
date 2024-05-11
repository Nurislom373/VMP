/**
 * P
 */
export class Pair {

  private _start: number;
  private _end: number;

  constructor(start: number, end: number) {
    this._start = start;
    this._end = end;
  }

  get start(): number {
    return this._start;
  }

  set start(value: number) {
    this._start = value;
  }

  get end(): number {
    return this._end;
  }

  set end(value: number) {
    this._end = value;
  }
}
