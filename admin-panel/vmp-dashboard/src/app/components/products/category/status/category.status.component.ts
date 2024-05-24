import {Component, Input} from "@angular/core";
import {CategoryStatus} from "../../../../models/category";
import {NgSwitch, NgSwitchCase, NgSwitchDefault} from "@angular/common";

@Component({
  selector: 'category-status',
  standalone: true,
  templateUrl: './category.status.component.html',
  imports: [
    NgSwitchCase,
    NgSwitchDefault,
    NgSwitch
  ],
  styleUrl: './category.status.component.css'
})
export class CategoryStatusComponent {

  @Input('status')
  status: CategoryStatus | undefined;
}
