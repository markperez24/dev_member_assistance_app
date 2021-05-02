import { Observable, Subject, defer } from "rxjs"
import { finalize, map } from "rxjs/operators"
import { NgxSpinnerService } from "ngx-spinner";

export const prepare = <T>(callback: () => void) => {
  return (source: Observable<T>): Observable<T> => defer(() => {
    callback();
    return source;
  });
};

export const indicator = <T>(indicator: Subject<boolean>) => {
  return (source: Observable<T>): Observable<T> => source.pipe(
    prepare(() => indicator.next(true)),
    finalize(() => indicator.unsubscribe())
  );
};

export const processing = <T>(spinnerService: NgxSpinnerService) => {
  return (source: Observable<T>): Observable<T> => source.pipe(
    prepare(() => spinnerService.show()),
    finalize(() => setTimeout(()=> {spinnerService.hide();}, 500))
  );
};

export const toClass = <T>(ClassType: { new(): T }) => (source: Observable<T>) => source.pipe(
  map(val => Object.assign(new ClassType(), val)));
