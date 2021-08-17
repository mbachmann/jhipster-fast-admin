import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAddressFaDetailComponent } from './contact-address-fa-detail.component';

describe('Component Tests', () => {
  describe('ContactAddressFa Management Detail Component', () => {
    let comp: ContactAddressFaDetailComponent;
    let fixture: ComponentFixture<ContactAddressFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactAddressFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactAddressFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactAddressFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactAddress).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
