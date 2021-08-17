import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { GenderType } from 'app/entities/enumerations/gender-type.model';
import { IContactPersonFa, ContactPersonFa } from '../contact-person-fa.model';

import { ContactPersonFaService } from './contact-person-fa.service';

describe('Service Tests', () => {
  describe('ContactPersonFa Service', () => {
    let service: ContactPersonFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IContactPersonFa;
    let expectedResult: IContactPersonFa | IContactPersonFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContactPersonFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        defaultPerson: false,
        name: 'AAAAAAA',
        surname: 'AAAAAAA',
        gender: GenderType.MALE,
        email: 'AAAAAAA',
        phone: 'AAAAAAA',
        department: 'AAAAAAA',
        salutation: 'AAAAAAA',
        showTitle: false,
        showDepartment: false,
        wantsNewsletter: false,
        birthDate: currentDate,
        birthPlace: 'AAAAAAA',
        placeOfOrigin: 'AAAAAAA',
        citizenCountry1: 'AAAAAAA',
        citizenCountry2: 'AAAAAAA',
        socialSecurityNumber: 'AAAAAAA',
        hobbies: 'AAAAAAA',
        dailyWork: 'AAAAAAA',
        contactAttribute01: 'AAAAAAA',
        avatarContentType: 'image/png',
        avatar: 'AAAAAAA',
        imageType: 'AAAAAAA',
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContactPersonFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            birthDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ContactPersonFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContactPersonFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultPerson: true,
            name: 'BBBBBB',
            surname: 'BBBBBB',
            gender: 'BBBBBB',
            email: 'BBBBBB',
            phone: 'BBBBBB',
            department: 'BBBBBB',
            salutation: 'BBBBBB',
            showTitle: true,
            showDepartment: true,
            wantsNewsletter: true,
            birthDate: currentDate.format(DATE_FORMAT),
            birthPlace: 'BBBBBB',
            placeOfOrigin: 'BBBBBB',
            citizenCountry1: 'BBBBBB',
            citizenCountry2: 'BBBBBB',
            socialSecurityNumber: 'BBBBBB',
            hobbies: 'BBBBBB',
            dailyWork: 'BBBBBB',
            contactAttribute01: 'BBBBBB',
            avatar: 'BBBBBB',
            imageType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContactPersonFa', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
            defaultPerson: true,
            gender: 'BBBBBB',
            phone: 'BBBBBB',
            wantsNewsletter: true,
            birthPlace: 'BBBBBB',
            placeOfOrigin: 'BBBBBB',
            citizenCountry1: 'BBBBBB',
            dailyWork: 'BBBBBB',
            avatar: 'BBBBBB',
          },
          new ContactPersonFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContactPersonFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            defaultPerson: true,
            name: 'BBBBBB',
            surname: 'BBBBBB',
            gender: 'BBBBBB',
            email: 'BBBBBB',
            phone: 'BBBBBB',
            department: 'BBBBBB',
            salutation: 'BBBBBB',
            showTitle: true,
            showDepartment: true,
            wantsNewsletter: true,
            birthDate: currentDate.format(DATE_FORMAT),
            birthPlace: 'BBBBBB',
            placeOfOrigin: 'BBBBBB',
            citizenCountry1: 'BBBBBB',
            citizenCountry2: 'BBBBBB',
            socialSecurityNumber: 'BBBBBB',
            hobbies: 'BBBBBB',
            dailyWork: 'BBBBBB',
            contactAttribute01: 'BBBBBB',
            avatar: 'BBBBBB',
            imageType: 'BBBBBB',
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContactPersonFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContactPersonFaToCollectionIfMissing', () => {
        it('should add a ContactPersonFa to an empty array', () => {
          const contactPerson: IContactPersonFa = { id: 123 };
          expectedResult = service.addContactPersonFaToCollectionIfMissing([], contactPerson);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactPerson);
        });

        it('should not add a ContactPersonFa to an array that contains it', () => {
          const contactPerson: IContactPersonFa = { id: 123 };
          const contactPersonCollection: IContactPersonFa[] = [
            {
              ...contactPerson,
            },
            { id: 456 },
          ];
          expectedResult = service.addContactPersonFaToCollectionIfMissing(contactPersonCollection, contactPerson);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContactPersonFa to an array that doesn't contain it", () => {
          const contactPerson: IContactPersonFa = { id: 123 };
          const contactPersonCollection: IContactPersonFa[] = [{ id: 456 }];
          expectedResult = service.addContactPersonFaToCollectionIfMissing(contactPersonCollection, contactPerson);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactPerson);
        });

        it('should add only unique ContactPersonFa to an array', () => {
          const contactPersonArray: IContactPersonFa[] = [{ id: 123 }, { id: 456 }, { id: 8507 }];
          const contactPersonCollection: IContactPersonFa[] = [{ id: 123 }];
          expectedResult = service.addContactPersonFaToCollectionIfMissing(contactPersonCollection, ...contactPersonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contactPerson: IContactPersonFa = { id: 123 };
          const contactPerson2: IContactPersonFa = { id: 456 };
          expectedResult = service.addContactPersonFaToCollectionIfMissing([], contactPerson, contactPerson2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contactPerson);
          expect(expectedResult).toContain(contactPerson2);
        });

        it('should accept null and undefined values', () => {
          const contactPerson: IContactPersonFa = { id: 123 };
          expectedResult = service.addContactPersonFaToCollectionIfMissing([], null, contactPerson, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contactPerson);
        });

        it('should return initial array if no ContactPersonFa is added', () => {
          const contactPersonCollection: IContactPersonFa[] = [{ id: 123 }];
          expectedResult = service.addContactPersonFaToCollectionIfMissing(contactPersonCollection, undefined, null);
          expect(expectedResult).toEqual(contactPersonCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
