import {Component, Input} from "@angular/core";
import {NzWaveDirective} from "ng-zorro-antd/core/wave";
import {NzButtonComponent, NzButtonSize} from "ng-zorro-antd/button";
import {NzAlign, NzGap} from "ng-zorro-antd/flex/typings";
import {NgIf} from "@angular/common";
import {NzFlexDirective} from "ng-zorro-antd/flex";

@Component({
  selector: 'custom-primary-button',
  standalone: true,
  templateUrl: './custom-primary-button.component.html',
  imports: [
    NzWaveDirective,
    NzButtonComponent,
    NgIf,
    NzFlexDirective
  ],
  styleUrl: './custom-primary-button.component.css'
})
export class CustomPrimaryButtonComponent {

  @Input() flex: Boolean = false;
  @Input() nzGap: NzGap = 'small';
  @Input() nzAlign: NzAlign = 'center';
  @Input() nzSize: NzButtonSize = 'default';

  primaryButtonClass: string = "ant-btn ant-btn-custom";

  buttonClass(): string {
    return this.primaryButtonClass;
  }
}
