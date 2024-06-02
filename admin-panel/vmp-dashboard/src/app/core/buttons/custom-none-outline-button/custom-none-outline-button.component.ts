import {Component, Input} from "@angular/core";
import {NzWaveDirective} from "ng-zorro-antd/core/wave";
import {NzButtonComponent, NzButtonSize} from "ng-zorro-antd/button";

@Component({
  selector: 'custom-none-outline-button',
  standalone: true,
  templateUrl: './custom-none-outline-button.component.html',
  imports: [
    NzWaveDirective,
    NzButtonComponent
  ],
  styleUrl: './custom-none-outline-button.component.css'
})
export class CustomNoneOutlineButtonComponent {

  @Input() nzSize: NzButtonSize = 'default';
}
