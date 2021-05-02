import {
  AfterViewInit,
  Directive,
  ElementRef,
  HostListener,
  Input,
  OnChanges,
  Optional,
  Renderer2
} from "@angular/core";
import { MatInput } from "@angular/material/input";

@Directive({
  selector: '[mobileNumber]'
})
export class MobileNumberDirective implements OnChanges {
  // Allow decimal numbers and negative values
  private regex: RegExp = new RegExp(/^\d{0,4}\-?\d{0,3}$\-?\d{0,4}$/g);
  // Allow key codes for special events. Reflect :
  // Backspace, tab, end, home
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', '-', 'ArrowLeft', 'ArrowRight', 'Del', 'Delete'];

  @Input() mobileNumber!: string;
  constructor(@Optional() private matInput: MatInput,
              private el: ElementRef,
              private renderer: Renderer2) {

  }

  /*constructor(private el: ElementRef) {
  }*/

 // ngAfterViewInit(): void {
 //   this.el.nativeElement.val;
 // }


  //@HostListener('keydown', ['$event'])
  //onKeyDown(event: KeyboardEvent) {
  ngOnChanges() {
    console.log(this.el.nativeElement.value);
    // Allow Backspace, tab, end, and home keys
    /*if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }*/
    let input: any;
    if (this.matInput) {
      // It's a Material Input
      input = this.matInput;
    } else {

      let current: string = this.el.nativeElement.value;
      const position = this.el.nativeElement.selectionStart;

      let f_val = current.replace(/\D[^\.]/g, "");
      f_val = f_val.slice(0, 3) + "-" + f_val.slice(3, 6) + "-" + f_val.slice(6);

      //const next: string = [current.slice(0, position), event.key == 'Decimal' ? '.' : event.key,
      // current.slice(position)].join('');
      if (f_val && !String(f_val).match(this.regex)) {
        this.el.nativeElement.preventDefault();
      }
    }
  }
}
