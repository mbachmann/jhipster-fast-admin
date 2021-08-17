import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IntervalType } from 'app/entities/enumerations/interval-type.model';
import { IContactReminderFa, ContactReminderFa } from '../contact-reminder-fa.model';

import { ContactReminderFaService } from './contact-reminder-fa.service';

describe('Service Tests', () => {
  describe('ContactReminderFa Service', () => {
    let service: ContactReminderFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactReminderFa;
    let expectedResult: IContactReminderFa | IContactReminderFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactReminderFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        contactId: 0,
        contactName: 'AAAAAAA',
        dateTime: currentDate,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        intervalValue: 0,
        intervalType: IntervalType.HOUR,
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactReminderFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.create(new ContactReminderFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactReminderFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactId: 1,
            contactName: 'BBBBBB',
            dateTime: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
            intervalType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactReminderFa', () => {
        const patchObject = Object.assign(
          {
            contactId: 1,
            contactName: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
          },
          new ContactReminderFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactReminderFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            contactId: 1,
            contactName: 'BBBBBB',
            dateTime: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            description: 'BBBBBB',
            intervalValue: 1,
            intervalType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactReminderFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactReminderFaToCollectionIfMissing', () => {
        it('should add a ContactReminderFa to an empty array', () => {
          const contactReminder: IContactReminderFa = { id: 123 };
          expectedResult = service.addContactReminderFaToCollectionIfMissing([], contactReminder);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should not add a ContactReminderFa to an array that contains it', () => {
          const contactReminder: IContactReminderFa = { id: 123 };
          const contactReminderCollection: IContactReminderFa[] = [
            {
              ...contactReminder,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactReminderFaToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactReminderFa to an array that doesn't contain it", () => {
          const contactReminder: IContactReminderFa = { id: 123 };
          const contactReminderCollection: IContactReminderFa[] = [{ id: 456 }];
          expectedResult = service.addContactReminderFaToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should add only unique ContactReminderFa to an array', () => {
          const contactReminderArray: IContactReminderFa[] = [{ id: 123 }, { id: 456 }, { id: 85579 }];
          const contactReminderCollection: IContactReminderFa[] = [{ id: 123 }];
          expectedResult = service.addContactReminderFaToCollectionIfMissing(contactReminderCollection, ...contactReminderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactReminder: IContactReminderFa = { id: 123 };
          const contactReminder2: IContactReminderFa = { id: 456 };
          expectedResult = service.addContactReminderFaToCollectionIfMissing([], contactReminder, contactReminder2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
          expect(expectedResult).toContain(contactReminder2);
        });

        it('should accept null and undefined values', () => {
          const contactReminder: IContactReminderFa = { id: 123 };
          expectedResult = service.addContactReminderFaToCollectionIfMissing([], null, contactReminder, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should return initial array if no ContactReminderFa is added', () => {
          const contactReminderCollection: IContactReminderFa[] = [{ id: 123 }];
          expectedResult = service.addContactReminderFaToCollectionIfMissing(contactReminderCollection, undefined, null);
          expect(expectedResult).toEqual(contactReminderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
