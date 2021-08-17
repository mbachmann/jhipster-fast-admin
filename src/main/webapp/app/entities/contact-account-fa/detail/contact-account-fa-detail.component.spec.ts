import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAccountFaDetailComponent } from './contact-account-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactAccountFa Management Detail Component', () => {
    let comp: ContactAccountFaDetailComponent;
    let fixture: ComponentFixture<ContactAccountFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactAccountFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactAccount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactAccountFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactAccountFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactAccount).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
