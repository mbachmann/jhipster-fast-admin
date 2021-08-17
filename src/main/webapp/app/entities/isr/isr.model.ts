import * as dayjs from 'dayjs';
import { IsrType } from 'app/entities/enumerations/isr-type.model';
import { IsrPosition } from 'app/entities/enumerations/isr-position.model';

export interface IIsr {
  id?: number;
  defaultIsr?: boolean | null;
  type?: IsrType | null;
  position?: IsrPosition | null;
  name?: string | null;
  bankName?: string | null;
  bankAddress?: string | null;
  recipientName?: string | null;
  recipientAddition?: string | null;
  recipientStreet?: string | null;
  recipientCity?: string | null;
  deliveryNumber?: string | null;
  iban?: string | null;
  subscriberNumber?: string | null;
  leftPrintAdjust?: number | null;
  topPrintAdjust?: number | null;
  created?: dayjs.Dayjs | null;
  inactiv?: boolean | null;
}

export class Isr implements IIsr {
  constructor(
    public id?: number,
    public defaultIsr?: boolean | null,
    public type?: IsrType | null,
    public position?: IsrPosition | null,
    public name?: string | null,
    public bankName?: string | null,
    public bankAddress?: string | null,
    public recipientName?: string | null,
    public recipientAddition?: string | null,
    public recipientStreet?: string | null,
    public recipientCity?: string | null,
    public deliveryNumber?: string | null,
    public iban?: string | null,
    public subscriberNumber?: string | null,
    public leftPrintAdjust?: number | null,
    public topPrintAdjust?: number | null,
    public created?: dayjs.Dayjs | null,
    public inactiv?: boolean | null
  ) {
    this.defaultIsr = this.defaultIsr ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getIsrIdentifier(isr: IIsr): number | undefined {
  return isr.id;
}
