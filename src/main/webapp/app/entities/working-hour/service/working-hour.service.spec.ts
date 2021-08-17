import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkingHour, WorkingHour } from '../working-hour.model';

import { WorkingHourService } from './working-hour.service';

describe('Service Tests', () => {
  describe('WorkingHour Service', () => {
    let service: WorkingHourService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkingHour;
    let expectedResult: IWorkingHour | IWorkingHour[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkingHourService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        userName: 'AAAAAAA',
        date: currentDate,
        timeStart: currentDate,
        timeEnd: currentDate,
        created: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WorkingHour', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            timeStart: currentDate,
            timeEnd: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.create(new WorkingHour()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkingHour', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            userName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            timeStart: currentDate,
            timeEnd: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkingHour', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new WorkingHour()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            timeStart: currentDate,
            timeEnd: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkingHour', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            userName: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            timeStart: currentDate.format(DATE_TIME_FORMAT),
            timeEnd: currentDate.format(DATE_TIME_FORMAT),
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            timeStart: currentDate,
            timeEnd: currentDate,
            created: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a WorkingHour', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkingHourToCollectionIfMissing', () => {
        it('should add a WorkingHour to an empty array', () => {
          const workingHour: IWorkingHour = { id: 123 };
          expectedResult = service.addWorkingHourToCollectionIfMissing([], workingHour);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingHour);
        });

        it('should not add a WorkingHour to an array that contains it', () => {
          const workingHour: IWorkingHour = { id: 123 };
          const workingHourCollection: IWorkingHour[] = [
            {
              ...workingHour,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkingHourToCollectionIfMissing(workingHourCollection, workingHour);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkingHour to an array that doesn't contain it", () => {
          const workingHour: IWorkingHour = { id: 123 };
          const workingHourCollection: IWorkingHour[] = [{ id: 456 }];
          expectedResult = service.addWorkingHourToCollectionIfMissing(workingHourCollection, workingHour);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingHour);
        });

        it('should add only unique WorkingHour to an array', () => {
          const workingHourArray: IWorkingHour[] = [{ id: 123 }, { id: 456 }, { id: 54617 }];
          const workingHourCollection: IWorkingHour[] = [{ id: 123 }];
          expectedResult = service.addWorkingHourToCollectionIfMissing(workingHourCollection, ...workingHourArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workingHour: IWorkingHour = { id: 123 };
          const workingHour2: IWorkingHour = { id: 456 };
          expectedResult = service.addWorkingHourToCollectionIfMissing([], workingHour, workingHour2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingHour);
          expect(expectedResult).toContain(workingHour2);
        });

        it('should accept null and undefined values', () => {
          const workingHour: IWorkingHour = { id: 123 };
          expectedResult = service.addWorkingHourToCollectionIfMissing([], null, workingHour, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingHour);
        });

        it('should return initial array if no WorkingHour is added', () => {
          const workingHourCollection: IWorkingHour[] = [{ id: 123 }];
          expectedResult = service.addWorkingHourToCollectionIfMissing(workingHourCollection, undefined, null);
          expect(expectedResult).toEqual(workingHourCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
