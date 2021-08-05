import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactPersonMySuffixDetailComponent } from './contact-person-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactPersonMySuffix Management Detail Component', () => {
    let comp: ContactPersonMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactPersonMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactPersonMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactPerson: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactPersonMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactPersonMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactPerson on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactPerson).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
