import * as dayjs from 'dayjs';
import { IContact } from 'app/entities/contact/contact.model';
import { IntervalType } from 'app/entities/enumerations/interval-type.model';

export interface IContactReminder {
  id?: number;
  remoteId?: number | null;
  contactName?: string | null;
  dateTime?: dayjs.Dayjs | null;
  title?: string | null;
  description?: string | null;
  intervalValue?: number | null;
  intervalType?: IntervalType | null;
  inactiv?: boolean | null;
  contact?: IContact | null;
}

export class ContactReminder implements IContactReminder {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public contactName?: string | null,
    public dateTime?: dayjs.Dayjs | null,
    public title?: string | null,
    public description?: string | null,
    public intervalValue?: number | null,
    public intervalType?: IntervalType | null,
    public inactiv?: boolean | null,
    public contact?: IContact | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getContactReminderIdentifier(contactReminder: IContactReminder): number | undefined {
  return contactReminder.id;
}
