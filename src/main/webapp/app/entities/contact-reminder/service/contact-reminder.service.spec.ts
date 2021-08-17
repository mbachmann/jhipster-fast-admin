import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IntervalType } from 'app/entities/enumerations/interval-type.model';
import { IContactReminder, ContactReminder } from '../contact-reminder.model';

import { ContactReminderService } from './contact-reminder.service';

describe('Service Tests', () => {
  describe('ContactReminder Service', () => {
    let service: ContactReminderService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactReminder;
    let expectedResult: IContactReminder | IContactReminder[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactReminderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
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

      it('should create a ContactReminder', () => {
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

        service.create(new ContactReminder()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactReminder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
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

      it('should partial update a ContactReminder', () => {
        const patchObject = Object.assign(
          {
            contactName: 'BBBBBB',
            dateTime: currentDate.format(DATE_TIME_FORMAT),
            intervalValue: 1,
            intervalType: 'BBBBBB',
          },
          new ContactReminder()
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

      it('should return a list of ContactReminder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
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

      it('should delete a ContactReminder', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactReminderToCollectionIfMissing', () => {
        it('should add a ContactReminder to an empty array', () => {
          const contactReminder: IContactReminder = { id: 123 };
          expectedResult = service.addContactReminderToCollectionIfMissing([], contactReminder);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should not add a ContactReminder to an array that contains it', () => {
          const contactReminder: IContactReminder = { id: 123 };
          const contactReminderCollection: IContactReminder[] = [
            {
              ...contactReminder,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactReminderToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactReminder to an array that doesn't contain it", () => {
          const contactReminder: IContactReminder = { id: 123 };
          const contactReminderCollection: IContactReminder[] = [{ id: 456 }];
          expectedResult = service.addContactReminderToCollectionIfMissing(contactReminderCollection, contactReminder);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should add only unique ContactReminder to an array', () => {
          const contactReminderArray: IContactReminder[] = [{ id: 123 }, { id: 456 }, { id: 16628 }];
          const contactReminderCollection: IContactReminder[] = [{ id: 123 }];
          expectedResult = service.addContactReminderToCollectionIfMissing(contactReminderCollection, ...contactReminderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactReminder: IContactReminder = { id: 123 };
          const contactReminder2: IContactReminder = { id: 456 };
          expectedResult = service.addContactReminderToCollectionIfMissing([], contactReminder, contactReminder2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactReminder);
          expect(expectedResult).toContain(contactReminder2);
        });

        it('should accept null and undefined values', () => {
          const contactReminder: IContactReminder = { id: 123 };
          expectedResult = service.addContactReminderToCollectionIfMissing([], null, contactReminder, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactReminder);
        });

        it('should return initial array if no ContactReminder is added', () => {
          const contactReminderCollection: IContactReminder[] = [{ id: 123 }];
          expectedResult = service.addContactReminderToCollectionIfMissing(contactReminderCollection, undefined, null);
          expect(expectedResult).toEqual(contactReminderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
