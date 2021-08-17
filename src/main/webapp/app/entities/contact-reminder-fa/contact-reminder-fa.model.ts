import * as dayjs from 'dayjs';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { IntervalType } from 'app/entities/enumerations/interval-type.model';

export interface IContactReminderFa {
  id?: number;
  remoteId?: number | null;
  contactId?: number | null;
  contactName?: string | null;
  dateTime?: dayjs.Dayjs | null;
  title?: string | null;
  description?: string | null;
  intervalValue?: number | null;
  intervalType?: IntervalType | null;
  inactiv?: boolean | null;
  contact?: IContactFa | null;
}

export class ContactReminderFa implements IContactReminderFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public contactId?: number | null,
    public contactName?: string | null,
    public dateTime?: dayjs.Dayjs | null,
    public title?: string | null,
    public description?: string | null,
    public intervalValue?: number | null,
    public intervalType?: IntervalType | null,
    public inactiv?: boolean | null,
    public contact?: IContactFa | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactReminderFaIdentifier(contactReminder: IContactReminderFa): number | undefined {
  return contactReminder.id;
}
