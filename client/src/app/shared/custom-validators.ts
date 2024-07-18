import { AbstractControl, ValidatorFn } from '@angular/forms';

export function compareWithWord(word: string): ValidatorFn {
	return (control: AbstractControl): { [key: string]: any } | null => {
		return control.value === word ? null : { 'isEqual': { value: 'Values do not match' } };
	};
}