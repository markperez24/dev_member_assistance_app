import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {
  @Input() isLoading: boolean | undefined;
  @Input() isLoadingLimitReached: boolean | undefined;

  constructor() {
  }

  ngOnInit(): void {
  }
}
