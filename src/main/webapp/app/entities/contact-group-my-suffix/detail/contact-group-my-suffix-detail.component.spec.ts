import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactGroupMySuffixDetailComponent } from './contact-group-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactGroupMySuffix Management Detail Component', () => {
    let comp: ContactGroupMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactGroupMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactGroupMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactGroupMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactGroupMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactGroup).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
