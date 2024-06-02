export interface StateAction {

  /**
   *
   */
  action(): void;

  /**
   *
   * @param property
   */
  changeProperties(property: any): void;
}
