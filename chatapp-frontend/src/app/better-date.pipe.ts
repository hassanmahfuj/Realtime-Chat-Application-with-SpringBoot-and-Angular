import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'betterDate',
})
export class BetterDatePipe implements PipeTransform {
  transform(value: string | Date): string {
    const date = new Date(value);
    const today = new Date();

    if (isToday(date, today)) {
      return formatTime(date);
    } else {
      return formatDate(date);
    }
  }
}

function isToday(date: Date, today: Date): boolean {
  return (
    date.getDate() === today.getDate() &&
    date.getMonth() === today.getMonth() &&
    date.getFullYear() === today.getFullYear()
  );
}

function formatTime(date: Date): string {
  return date.toLocaleTimeString();
}

function formatDate(date: Date): string {
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
